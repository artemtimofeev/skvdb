import {Button, Modal} from "react-bootstrap";
import DeleteInstanceRequest from "../../api/instances/DeleteInstanceRequest";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";

function DeleteInstanceModal({show, setShow}) {
    const [redirect, setRedirect] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (redirect) {
            navigate("/");
        }
    }, [redirect, navigate]);

    const {instanceId} = useParams();

    return <Modal show={show} onHide={() => {setShow(false)}}>
        <Modal.Header closeButton>
            <Modal.Title>Delete this instance</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure?</Modal.Body>
        <Modal.Footer>
            <Button variant="outline-danger" onClick={() => {
                DeleteInstanceRequest(instanceId).then(response => {
                    if (response.result === "OK") {
                        setRedirect(true);
                    }
                })
            }}>
                Yes
            </Button>
            <Button variant="outline-success" onClick={() => {setShow(false)}}>
                Cancel
            </Button>
        </Modal.Footer>
    </Modal>
}

export default DeleteInstanceModal;