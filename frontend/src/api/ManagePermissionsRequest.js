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

    const response = await fetch(`${HTTP_SCHEMA}://${HOST}/api/instance/${instanceId}/permission`, options);
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const responseBody = await response.json()
    if (responseBody.result === "Error") {
        throw new Error(`Error: ${responseBody.error}`)
    }
    return responseBody;
}

export default ManagePermissionsRequest;