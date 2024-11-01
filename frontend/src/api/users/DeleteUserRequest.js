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

    try {
        const response = await fetch(`${HTTP_SCHEMA}://${HOST}/api/instance/${instanceId}/user/${username}`, options);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export default DeleteUserRequest;