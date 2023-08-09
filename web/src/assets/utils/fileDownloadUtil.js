import request from '@/assets/utils/request';
/* eslint-disable */
function fileDownloadUtil(filePath){
    if(filePath!=undefined && filePath!=null && filePath!=''){ 
        let fileName = filePath.substr(filePath.lastIndexOf('\\')+1);
        let url = '/file/download?filePath=' + encodeURI(encodeURI(filePath)); 
        request({
            url: url,
            responseType: 'blob'
        }).then(res => { 
            let url = window.URL.createObjectURL(new Blob([res.data]))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName); 
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        });
    }
}

export { fileDownloadUtil }