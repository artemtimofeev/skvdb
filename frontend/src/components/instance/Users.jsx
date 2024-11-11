import {Button, Table} from "react-bootstrap";
import {useEffect, useState} from "react";
import DeleteUserModal from "../modals/user/DeleteUserModal";
import CreateUserModal from "../modals/user/CreateUserModal";
import ManagePermissionsUserModal from "../modals/user/ManagePermissionsUserModal";
import GetAllUsersRequest from "../../api/users/GetAllUsersRequest";
import GetAllTablesRequest from "../../api/tables/GetAllTablesRequest";
import {useParams} from "react-router-dom";

function Users() {
    const [showDelete, setShowDelete] = useState(false);
    const [showCreate, setShowCreate] = useState(false);
    const [showPermissions, setShowPermissions] = useState(false);
    const [username, setUsername] = useState('');
    const [users, setUsers] = useState([]);

    const {instanceId} = useParams();

    useEffect(()=>{
        GetAllUsersRequest(instanceId).then(response => {
            setUsers(response.users);
        }).catch(error => console.log(error))
    }, [instanceId]);

    const [tables, setTables]= useState([]);

    useEffect(() => {
        GetAllTablesRequest(instanceId).then(
            response => {
                setTables(response.tables);
            }
        )
    }, [instanceId]);

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
            {
                users.map((user, index) => {
                    if (user.name === "admin") {
                        return <></>
                    }
                    return <tr>
                        <td>{user.name}</td>
                        <td>{user.permissions}</td>
                        <td>{user.isSuperuser.toString()}</td>
                        <td>
                            <Button variant="outline-danger" onClick={() => {
                                setShowDelete(true);
                                setUsername(user.name);}}
                            >Delete user</Button>{' '}
                            <Button variant="outline-primary" onClick={() => {
                                setShowPermissions(true);
                                setUsername(user.name)}}
                            >Manage permissions</Button>{' '}
                        </td>
                    </tr>
                })
            }
            </tbody>
        </Table>

        <div className="d-flex justify-content-center">
            <Button variant="outline-primary" className="mt-2" onClick={()=> setShowCreate(true)}>Create new user</Button>
        </div>

        <DeleteUserModal showDelete={showDelete} setShowDelete={setShowDelete} username={username}/>
        <CreateUserModal showCreate={showCreate} setShowCreate={setShowCreate}/>
        <ManagePermissionsUserModal showPermissions={showPermissions} setShowPermissions={setShowPermissions} username={username} tables={tables}/>
    </>;
}

export default Users;