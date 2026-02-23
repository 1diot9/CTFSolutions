// src/main/resources/static/js/scripts.js

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('dbtestForm');
    const resultDiv = document.getElementById('result');

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // 阻止表单默认提交行为

        const data = document.getElementById('data').value;
        fetch('/dbtest', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                'data': data
            })
        })
            .then(response => response.text())
            .then(message => {
                // 清空之前的结果
                resultDiv.innerHTML = '';

                // 创建一个新的 alert div
                const alertDiv = document.createElement('div');
                alertDiv.classList.add('alert');
                alertDiv.classList.add(message.includes('成功') ? 'alert-success' : 'alert-danger');
                alertDiv.setAttribute('role', 'alert');

                if (message.includes('成功')) {
                    alertDiv.innerHTML = `<h4 class="alert-heading">连接成功！</h4><p>${message}</p>`;
                } else {
                    alertDiv.innerHTML = `<h4 class="alert-heading">连接失败！</h4><p>${message}</p>`;
                }

                // 添加到结果 div 中
                resultDiv.appendChild(alertDiv);
            })
            .catch(error => {
                console.error('Error:', error);
                resultDiv.innerHTML = `
                <div class="alert alert-danger" role="alert">
                    <h4 class="alert-heading">请求失败！</h4>
                    <p>发生错误，请稍后再试。</p>
                </div>
            `;
            });
    });
});
