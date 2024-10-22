import Header from "../components/Header";
import {Button, Container, Table} from "react-bootstrap";
import {Link} from "react-router-dom";

function Instances() {

    return <>
        <Header/>
        <Container className="mt-4">
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>id</th>
                    <th>name</th>
                    <th>ip</th>
                    <th>port</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td><Link to='/instance/1'>test-instance</Link></td>
                    <td>192.168.1.1</td>
                    <td>4040</td>
                </tr>
                </tbody>
            </Table>

            <div className="d-flex justify-content-center">
                <Button variant="outline-primary" className="mt-2">Create new instance</Button>
            </div>
        </Container>
    </>;
}

export {Instances};