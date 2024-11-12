import {Button, Table} from "react-bootstrap";
import {useEffect, useState} from "react";
import DeleteInstanceModal from "../modals/DeleteInstanceModal";
import GetAllOperationsRequest from "../../api/GetAllOperationsRequest";
import {useParams} from "react-router-dom";

function Operations() {
    const [show, setShow] = useState(false);
    const [operations, setOperations] = useState([]);

    const {instanceId} = useParams();

    /*useEffect(() => {
        GetAllOperationsRequest(instanceId).then(
            response => {
                setOperations(response.result);
            }
        )
    }, [instanceId]);*/

    return <>
        <Button variant="outline-danger" onClick={() => setShow(true)}>Delete instance</Button>
        {/*<Table striped bordered hover className='mt-3'>
            <thead>
            <tr>
                <th>id</th>
                <th>operation</th>
                <th>timestamp</th>
            </tr>
            </thead>
            <tbody>
            {operations.map((operation, index) => {
                return <tr key={index}>
                    <td>{operation.id}</td>
                    <td>{operation.operation}</td>
                    <td>{operation.timestamp}</td>
                </tr>
            })}
            </tbody>
        </Table>*/}
        <DeleteInstanceModal show={show} setShow={setShow}/>
    </>;
}

export default Operations;