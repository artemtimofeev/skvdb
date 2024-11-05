import Header from "../components/Header";
import {Container, Table} from "react-bootstrap";
import {useEffect, useState} from "react";
import GetAllBills from "../api/billing/GetAllBills";
import GetBalance from "../api/billing/GetBalance";

function Billing() {
    const [bills, setBills] = useState([]);
    const [balance, setBalance] = useState("");

    useEffect(() => {
        GetBalance().then(
            response => {
                setBalance(response.balance);
            }
        ).catch((error) => {console.log(error)})
        GetAllBills().then(
            response => {
                setBills(response.transactions);
            }
        ).catch((error) => {console.log(error)})
    }, []);
    return <>
        <Header/>
        <Container className="mt-4">
            <h3>Balance: {balance} T</h3>
            <Table striped bordered hover className="mt-4">
                <thead>
                <tr>
                    <th>transaction_id</th>
                    <th>type</th>
                    <th>description</th>
                    <th>amount</th>
                </tr>
                </thead>
                <tbody>
                {
                    bills.map((bill, index) => {
                        return <tr key={index}>
                            <td>{bill.transactionId}</td>
                            <td>{bill.type}</td>
                            <td>{bill.description}</td>
                            <td>{bill.amount}</td>
                        </tr>
                    })
                }
                </tbody>
            </Table>
        </Container>
    </>
}

export {Billing};