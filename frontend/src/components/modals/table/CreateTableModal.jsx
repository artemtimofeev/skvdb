import {Alert, Button, Form, Modal} from "react-bootstrap";

function CreateTableModal({showCreate, setShowCreate}) {
    return <Modal show={showCreate} onHide={() => {setShowCreate(false)}}>
        <Form>
            <Modal.Header closeButton>
                <Modal.Title>Create new table</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Table name</Form.Label>
                    <Form.Control />
                </Form.Group>
                <Alert key="danger" variant="danger">
                    Error
                </Alert>
            </Modal.Body>
            <Modal.Footer>
                <Button type="submit" variant="outline-primary">
                    Create
                </Button>
                <Button variant="outline-secondary" onClick={() => {setShowCreate(false)}}>
                    Cancel
                </Button>
            </Modal.Footer>
        </Form>
    </Modal>
}

export default CreateTableModal;