import {Button, Modal} from "react-bootstrap";
import DeleteTableRequest from "../../../api/tables/DeleteTableRequest";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";

function DeleteTableModal({showDelete, setShowDelete, tableName}) {
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
            <Modal.Title>Delete table {tableName}</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure?</Modal.Body>
        <Modal.Footer>
            <Button variant="outline-danger" onClick={() => {
                DeleteTableRequest(tableName, instanceId).then(response => {
                    setRedirect(true);
                });
            }}>
                Yes
            </Button>
            <Button variant="outline-success" onClick={() => {
                setShowDelete(false);
            }}>
                Cancel
            </Button>
        </Modal.Footer>
    </Modal>
}

export default DeleteTableModal;