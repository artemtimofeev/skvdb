import React from 'react';
import { Container, Card } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

const LogsWindow = ({content}) => {
    return (
        <Container>
            <Card style={{ height: '500px', overflow: 'scroll', background: "black", color: "rgb(169, 183, 198)"}}>
                <Card.Body>
                    <Card.Text style={{whiteSpace: 'pre-wrap'}}>
                        {content}
                    </Card.Text>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default LogsWindow;