import {Button, Modal} from "react-bootstrap";

function DeleteUserModal({showDelete, setShowDelete}) {
    return <Modal show={showDelete} onHide={() => {
        setShowDelete(false)
    }}>
        <Modal.Header closeButton>
            <Modal.Title>Delete user [tableName]</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure?</Modal.Body>
        <Modal.Footer>
            <Button variant="outline-danger" onClick={() => {
                setShowDelete(false)
            }}>
                Yes
            </Button>
            <Button variant="outline-success" onClick={() => {
                setShowDelete(false)
            }}>
                Cancel
            </Button>
        </Modal.Footer>
    </Modal>
}

export default DeleteUserModal;