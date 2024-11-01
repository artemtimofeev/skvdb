import GetToken from "../storage/GetToken";
import {HOST, HTTP_SCHEMA} from "./Settings";

async function ManagePermissionsRequest(tableName, username, isOwner, isReader, isWriter, instanceId) {
    const options = {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': GetToken()
        },
        body: JSON.stringify({
            tableName: tableName,
            username: username,
            isOwner: isOwner,
            isReader: isReader,
            isWriter: isWriter
        })
    };

    try {
        const response = await fetch(`${HTTP_SCHEMA}://${HOST}/api/instance/${instanceId}/permission`, options);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

export default ManagePermissionsRequest;