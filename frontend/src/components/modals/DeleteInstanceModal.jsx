import {Button, Modal} from "react-bootstrap";

function DeleteInstanceModal({show, setShow}) {
    return <Modal show={show} onHide={() => {setShow(false)}}>
        <Modal.Header closeButton>
            <Modal.Title>Delete this instance</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure?</Modal.Body>
        <Modal.Footer>
            <Button variant="outline-danger" onClick={() => {setShow(false)}}>
                Yes
            </Button>
            <Button variant="outline-success" onClick={() => {setShow(false)}}>
                Cancel
            </Button>
        </Modal.Footer>
    </Modal>
}

export default DeleteInstanceModal;