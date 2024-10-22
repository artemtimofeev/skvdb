import {Button, Table} from "react-bootstrap";
import {useState} from "react";
import DeleteUserModal from "../modals/user/DeleteUserModal";
import CreateUserModal from "../modals/user/CreateUserModal";
import ManagePermissionsUserModal from "../modals/user/ManagePermissionsUserModal";

function Users() {
    const [showDelete, setShowDelete] = useState(false);
    const [showCreate, setShowCreate] = useState(false);
    const [showPermissions, setShowPermissions] = useState(false);

    return <>
        <Table striped bordered hover>
            <thead>
            <tr>
                <th>name</th>
                <th>permissions</th>
                <th>is superuser</th>
                <th>operations</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>admin</td>
                <td>Table [] : OWNER, READ, WRITE</td>
                <td>true</td>
                <td>
                    <Button variant="outline-danger" onClick={() => setShowDelete(true)}>Delete user</Button>{' '}
                    <Button variant="outline-primary" onClick={()=> setShowPermissions(true)}>Manage permissions</Button>{' '}
                </td>
            </tr>
            </tbody>
        </Table>

        <div className="d-flex justify-content-center">
            <Button variant="outline-primary" className="mt-2" onClick={()=> setShowCreate(true)}>Create new user</Button>
        </div>

        <DeleteUserModal showDelete={showDelete} setShowDelete={setShowDelete}/>
        <CreateUserModal showCreate={showCreate} setShowCreate={setShowCreate}/>
        <ManagePermissionsUserModal showPermissions={showPermissions} setShowPermissions={setShowPermissions}/>
    </>;
}

export default Users;