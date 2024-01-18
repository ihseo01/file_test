const uploadBtn = document.getElementById('upload-btn');

if (uploadBtn) {
    uploadBtn.addEventListener('click', event => {
        console.log('파일선택');
        let inputFile = document.getElementById('upload-file');
        let formData = new FormData();
        let files = inputFile.files;

        formData.append("uploadFile", files[0]);

        fetch('/api/file/upload', {
            method: 'POST',
            body: formData
        })
            .then((response) => {
                console.log(response);
                alert('파일이 업로드 되었습니다.');
                location.replace('/fileList');
            })
    });
}

const deleteBtn = document.getElementsByClassName('delete-btn');
for(let i=0; i<deleteBtn.length; i++) {
    deleteBtn[i].addEventListener('click', function () {
        const parentNode = this.parentNode;
        const lastNode = parentNode.lastElementChild;
        console.log(lastNode['value']);

        fetch(`/api/file/delete/${lastNode['value']}`, {
           method: 'GET',
        })
            .then((response) => {
                console.log(response);
                alert('파일이 삭제되었습니다.');
                location.replace('/fileList');
            });
    });
}