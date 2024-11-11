import Header from "../components/Header";
import {Button, Container, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import CreateNewInstanceModal from "../components/modals/CreateNewInstanceModal";
import {useEffect, useState} from "react";
import GetAllInstancesRequest from "../api/instances/GetAllInstancesRequest";

function Instances() {
    const [showCreate, setShowCreate] = useState(false);
    const [instances, setInstances] = useState([]);

    useEffect(() => {
        GetAllInstancesRequest().then(
            response => {
                console.log(response)
                setInstances(response.instances);
            }
        ).catch(error => {
            console.log(error)
        })
    }, []);

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
                    <th>status</th>
                    <th>rate (T per 5 minutes)</th>
                </tr>
                </thead>
                <tbody>
                {
                    instances.map((instance, index) => {
                        return <tr>
                            <td>{instance.id}</td>
                            {instance.status !== "DELETED" ?
                                <td><Link to={'/instance/' + instance.id + '/'}>{instance.name}</Link></td> :
                                <td>{instance.name}</td>}
                            <td>{instance.ip}</td>
                            <td>{instance.port}</td>
                            <td>{instance.status}</td>
                            <td>{instance.rate}</td>
                        </tr>
                    })
                }
                </tbody>
            </Table>

            <div className="d-flex justify-content-center">
                <Button variant="outline-primary" className="mt-2" onClick={() => setShowCreate(true)}>Create new instance</Button>
            </div>

            <CreateNewInstanceModal showCreate={showCreate} setShowCreate={setShowCreate}/>
        </Container>
    </>;
}

export {Instances};