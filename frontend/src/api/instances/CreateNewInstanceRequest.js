import GetToken from "../../storage/GetToken";
import {HOST, HTTP_SCHEMA} from "../Settings";

async function CreateNewInstanceRequest(instanceName) {
    const options = {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': GetToken()
        },
        body: JSON.stringify({instanceName: instanceName})
    };

    try {
        const response = await fetch(`${HTTP_SCHEMA}://${HOST}/api/instance`, options);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export default CreateNewInstanceRequest;