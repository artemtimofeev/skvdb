import GetToken from "../../storage/GetToken";
import {HOST, HTTP_SCHEMA} from "../Settings";

async function CreateNewTableRequest(tableName, instanceId) {
    const options = {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': GetToken()
        },
        body: JSON.stringify({tableName: tableName})
    };

    try {
        const response = await fetch(`${HTTP_SCHEMA}://${HOST}/api/instance/${instanceId}/table`, options);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export default CreateNewTableRequest;