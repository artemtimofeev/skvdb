function IsLoggedIn() {
    return window.localStorage.getItem("token") !== null;
}

export default IsLoggedIn;