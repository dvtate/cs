
//
const cs425 = {
    hostname: "http://dvtate.com:4250",
    getCookie: cname => {
        const name = cname + "=";
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');
        for(var i = 0; i <ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) == ' ')
                c = c.substring(1);
            if (c.indexOf(name) == 0)
                return c.substring(name.length, c.length);
        }
        return "";
    },

    redirect: url => window.location = url,

    api: {

        // make a post request
        post : async (endpoint, body) => {
            try {
                const resp = await fetch(cs425.hostname + endpoint, {
                    method: 'POST',
                    mode: 'cors',
                    headers: {
                      'Accept': 'application/json',
                      'Content-Type': 'application/json',
                      'Authorization': `Bearer ${ cs425.getCookie( 'authToken' ) }`,
                    },
                    body: JSON.stringify(body)
                });
                return await resp.json();
            } catch (e) {
                throw e;
            }
        },

        // make a get request
        get : async endpoint => {
            try {
                const resp = await fetch(cs425.hostname + endpoint, {
                    method: 'GET',
                    mode: 'cors',
                    headers: {
                      'Accept': 'application/json',
                      'Content-Type': 'application/json',
                      'Authorization': `Bearer ${cs425.getCookie('authToken')}`,
                    },
                });
                return await resp.json();
            } catch (e) {
                throw e;
            }
        }
    }
};


// automatically check authentication, if it's invalid, redirect to login page
(function checkAuth() {
    let token;
    if (!(token = cs425.getCookie('authToken')))
        window.location = 'login.html';

})();
