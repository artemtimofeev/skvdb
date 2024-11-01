import {HOST, HTTP_SCHEMA} from "../Settings";

async function SignupRequest(username, password) {
    const options = {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username: username, password: password})
    };

    const response = await fetch(`${HTTP_SCHEMA}://${HOST}/api/signup`, options);
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const responseBody = await response.json()
    if (responseBody.result === "Error") {
        throw new Error(`Error: ${responseBody.error}`)
    }
    return responseBody;
}

export default SignupRequest;