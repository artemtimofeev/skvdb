import {Button, Form, Modal} from "react-bootstrap";
import DropdownInputUser from "./DropdownInputUser";

function ManagePermissionsUserModal({showPermissions, setShowPermissions}){
    return <Modal show={showPermissions} onHide={() => {
        setShowPermissions(false)
    }}>
        <Modal.Header closeButton>
            <Modal.Title>Manage permissions [userName]</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <DropdownInputUser/>
            <Form>
                <Form.Group className="mb-3 mt-3" controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="OWNER" />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="READ" />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicCheckbox">
                    <Form.Check type="checkbox" label="WRITE" />
                </Form.Group>
            </Form>
        </Modal.Body>
        <Modal.Footer>
            <Button variant="outline-primary" onClick={() => {
                setShowPermissions(false)
            }}>
                Yes
            </Button>
            <Button variant="outline-secondary" onClick={() => {
                setShowPermissions(false)
            }}>
                Cancel
            </Button>
        </Modal.Footer>
    </Modal>
}

export default ManagePermissionsUserModal;