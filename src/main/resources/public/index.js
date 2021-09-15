var index = (function (){
    function change(entrada) {
        if (entrada != null && entrada != "") {
            getImage(entrada, changeImage);
        }
    }

    function getImage(entrada, callback) {
        const promise = new Promise((resolve, reject) => {
            $.ajax({
                url: "/appuser/" + entrada
            }).done(function (response) {
                resolve(response);
            }).fail(function (msg) {
                reject(msg);
            });
        });

        promise.then(res => {
            callback(res);
        });
    }

    function changeImage(data){
        document.getElementById("img").src=data;
    }

        return {
            change: change,
            getImage: getImage,
            changeImage: changeImage
        }

    }
)()