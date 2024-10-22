import React, {useState} from "react";
import {Dropdown, Form, InputGroup} from "react-bootstrap";

function DropdownInputUser() {
    const [inputValue, setInputValue] = useState('');
    const [showDropdown, setShowDropdown] = useState(false);
    const options = ['Option 1', 'Option 2', 'Option 3'];

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
        setShowDropdown(e.target.value.length > 0);
    };

    const handleSelect = (option) => {
        setInputValue(option);
        setShowDropdown(false);
    };

    return (
        <div>
            <Form.Group>
                <InputGroup>
                    <Form.Control
                        value={inputValue}
                        onChange={handleInputChange}
                        placeholder="Table name"
                        aria-label="Table name"
                        aria-describedby="basic-addon1"
                    />
                </InputGroup>
                {showDropdown && (
                    <Dropdown>
                        <Dropdown.Menu show>
                            {options
                                .filter(option => option.toLowerCase().includes(inputValue.toLowerCase()))
                                .map((option, index) => (
                                    <Dropdown.Item key={index} onClick={() => handleSelect(option)}>
                                        {option}
                                    </Dropdown.Item>
                                ))}
                        </Dropdown.Menu>
                    </Dropdown>
                )}
            </Form.Group>
        </div>
    );
}

export default DropdownInputUser;