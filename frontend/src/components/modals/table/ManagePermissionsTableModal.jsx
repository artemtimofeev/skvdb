import {Button, Form, Modal} from "react-bootstrap";
import DropdownInputTable from "./DropdownInputTable";

function ManagePermissionsTableModal({showPermissions, setShowPermissions}) {
    return <Modal show={showPermissions} onHide={() => {
        setShowPermissions(false)
    }}>
        <Modal.Header closeButton>
            <Modal.Title>Manage permissions [tableName]</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <DropdownInputTable/>
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

export default ManagePermissionsTableModal;