import Header from "../components/Header";
import {Container, Tab, Tabs} from "react-bootstrap";
import Tables from "../components/instance/Tables";
import Users from "../components/instance/Users";
import DbLogs from "../components/instance/DbLogs";
import Operations from "../components/instance/Operations";

function Instance() {
    return <>
        <Header/>
        <Container className="mt-4">
            <Tabs defaultActiveKey="users" className="mb-3">
                <Tab eventKey="users" title="Users">
                    <Users/>
                </Tab>
                <Tab eventKey="tables" title="Tables">
                    <Tables/>
                </Tab>
                <Tab eventKey="logs" title="DB Logs">
                    <DbLogs/>
                </Tab>
                <Tab eventKey="operations" title="Operations">
                    <Operations/>
                </Tab>
            </Tabs>
        </Container>
    </>;
}

export {Instance};