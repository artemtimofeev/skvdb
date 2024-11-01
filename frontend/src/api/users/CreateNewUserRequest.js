import GetToken from "../../storage/GetToken";
import {HOST, HTTP_SCHEMA} from "../Settings";

async function CreateNewUserRequest(username, password, isSuperuser, instanceId){
    const options = {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': GetToken()
        },
        body: JSON.stringify({username: username, password: password, isSuperuser: isSuperuser})
    };

    try {
        const response = await fetch(`${HTTP_SCHEMA}://${HOST}/api/instance/${instanceId}/user`, options);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export default CreateNewUserRequest;