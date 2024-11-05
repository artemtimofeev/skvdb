import GetToken from "../../storage/GetToken";
import {HOST, HTTP_SCHEMA} from "../Settings";

async function GetBalance() {
    const options = {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': GetToken()
        },
    };

    const response = await fetch(`${HTTP_SCHEMA}://${HOST}/api/balance`, options);
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const responseBody = await response.json()
    if (responseBody.result === "Error") {
        throw new Error(`Error: ${responseBody.error}`)
    }
    return responseBody;
}

export default GetBalance;