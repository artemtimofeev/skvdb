import {Button, Table} from "react-bootstrap";
import {useEffect, useState} from "react";
import DeleteTableModal from "../modals/table/DeleteTableModal";
import CreateTableModal from "../modals/table/CreateTableModal";
import ManagePermissionsTableModal from "../modals/table/ManagePermissionsTableModal";
import GetAllTablesRequest from "../../api/tables/GetAllTablesRequest";
import GetAllUsersRequest from "../../api/users/GetAllUsersRequest";
import {useParams} from "react-router-dom";

function Tables() {
    const [tables, setTables]= useState([]);
    const [tableName, setTableName] = useState('');
    const [showDelete, setShowDelete] = useState(false);
    const [showCreate, setShowCreate] = useState(false);
    const [showPermissions, setShowPermissions] = useState(false);

    const {instanceId} = useParams();

    useEffect(() => {
        GetAllTablesRequest(instanceId).then(
            response => {
                setTables(response.tables);
            }
        )
    }, [instanceId]);

    const [users, setUsers] = useState([]);

    useEffect(()=>{
        GetAllUsersRequest(instanceId).then(response => {
            setUsers(response.users);
        }).catch(error => console.log(error))
    }, [instanceId]);

    return <>
        <Table striped bordered hover>
            <thead>
            <tr>
                <th>name</th>
                {/*<th>permissions</th>*/}
                <th>operations</th>
            </tr>
            </thead>
            <tbody>
            {tables.map((table, index) => {
                return <tr key={index}>
                    <td>{table.name}</td>
                    {/*<td>{table.permissions}</td>*/}
                    <td>
                        <Button variant="outline-danger" onClick={() => {
                            setTableName(table.name);
                            setShowDelete(true);
                        }}>Delete table</Button>{' '}
                        {/*<Button variant="outline-primary" onClick={() => {
                            setTableName(table.name);
                            setShowPermissions(true)
                        }}>Manage permissions</Button>{' '}*/}
                    </td>
                </tr>
            })}
            </tbody>
        </Table>

        <div className="d-flex justify-content-center">
            <Button variant="outline-primary" className="mt-2" onClick={()=>setShowCreate(true)}>Create new table</Button>
        </div>

        <DeleteTableModal showDelete={showDelete} setShowDelete={setShowDelete} tableName={tableName}/>
        <CreateTableModal showCreate={showCreate} setShowCreate={setShowCreate}/>
        <ManagePermissionsTableModal showPermissions={showPermissions} setShowPermissions={setShowPermissions} tableName={tableName} users={users}/>
    </>;
}

export default Tables;