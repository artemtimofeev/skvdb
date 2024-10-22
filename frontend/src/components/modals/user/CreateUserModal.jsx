import {Alert, Button, Form, Modal} from "react-bootstrap";

function CreateUserModal({showCreate, setShowCreate}){
    return <Modal show={showCreate} onHide={() => {setShowCreate(false)}}>
        <Form>
            <Modal.Header closeButton>
                <Modal.Title>Create new user</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Username</Form.Label>
                    <Form.Control />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Password</Form.Label>
                    <Form.Control />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="Superuser" />
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

export default CreateUserModal;