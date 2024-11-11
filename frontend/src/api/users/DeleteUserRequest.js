import GetToken from "../../storage/GetToken";
import {HOST, HTTP_SCHEMA} from "../Settings";

async function DeleteUserRequest(username, instanceId) {
    const options = {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': GetToken()
        }
    };

    const response = await fetch(`${HTTP_SCHEMA}://${HOST}/api/instance/${instanceId}/user/${username}`, options);
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const responseBody = await response.json()
    if (responseBody.result === "Error") {
        throw new Error(`Error: ${responseBody.error}`)
    }
    return responseBody;
}

export default DeleteUserRequest;