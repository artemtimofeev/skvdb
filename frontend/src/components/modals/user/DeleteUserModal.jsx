import {Button, Modal} from "react-bootstrap";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import DeleteUserRequest from "../../../api/users/DeleteUserRequest";

function DeleteUserModal({showDelete, setShowDelete, username}) {
    const [redirect, setRedirect] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (redirect) {
            navigate("/");
        }
    }, [redirect, navigate]);

    const {instanceId} = useParams();

    return <Modal show={showDelete} onHide={() => {
        setShowDelete(false)
    }}>
        <Modal.Header closeButton>
            <Modal.Title>Delete user {username}</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure?</Modal.Body>
        <Modal.Footer>
            <Button variant="outline-danger" onClick={() => {
                DeleteUserRequest(username, instanceId).then(() => {
                        setRedirect(true);
                });
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