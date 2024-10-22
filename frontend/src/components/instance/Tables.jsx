import {Button, Table} from "react-bootstrap";
import {useState} from "react";
import DeleteTableModal from "../modals/table/DeleteTableModal";
import CreateTableModal from "../modals/table/CreateTableModal";
import ManagePermissionsTableModal from "../modals/table/ManagePermissionsTableModal";

function Tables() {
    const [showDelete, setShowDelete] = useState(false);
    const [showCreate, setShowCreate] = useState(false);
    const [showPermissions, setShowPermissions] = useState(false);

    return <>
        <Table striped bordered hover>
            <thead>
            <tr>
                <th>name</th>
                <th>permissions</th>
                <th>operations</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>testTable</td>
                <td>User [] : OWNER, READ, WRITE</td>
                <td><Button variant="outline-danger" onClick={() => setShowDelete(true)}>Delete table</Button>{' '}<Button
                    variant="outline-primary" onClick={() => setShowPermissions(true)}>Manage permissions</Button>{' '}</td>
            </tr>
            </tbody>
        </Table>

        <div className="d-flex justify-content-center">
            <Button variant="outline-primary" className="mt-2" onClick={()=>setShowCreate(true)}>Create new table</Button>
        </div>

        <DeleteTableModal showDelete={showDelete} setShowDelete={setShowDelete}/>
        <CreateTableModal showCreate={showCreate} setShowCreate={setShowCreate}/>
        <ManagePermissionsTableModal showPermissions={showPermissions} setShowPermissions={setShowPermissions}/>
    </>;
}

export default Tables;