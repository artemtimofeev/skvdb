import {Button, Table} from "react-bootstrap";
import {useState} from "react";
import DeleteInstanceModal from "../modals/DeleteInstanceModal";

function Operations() {
    const [show, setShow] = useState(false);
    return <>
        <Button variant="outline-danger" onClick={() => setShow(true)}>Delete instance</Button>
        <Table striped bordered hover className='mt-3'>
            <thead>
            <tr>
                <th>id</th>
                <th>operation</th>
                <th>timestamp</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>1ddd112</td>
                <td>Instance created</td>
                <td>19216811</td>
            </tr>
            </tbody>
        </Table>
        <DeleteInstanceModal show={show} setShow={setShow}/>
    </>;
}

export default Operations;