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