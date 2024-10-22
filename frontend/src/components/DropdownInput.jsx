import React, { useState } from 'react';
import {Form, Dropdown, InputGroup} from 'react-bootstrap';

const DropdownInput = () => {
    const [inputValue, setInputValue] = useState('');
    const [showDropdown, setShowDropdown] = useState(false);
    const options = ['Option 1', 'Option 2', 'Option 3'];

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
        setShowDropdown(e.target.value.length > 0); // Показывать выпадающий список, если есть ввод
    };

    const handleSelect = (option) => {
        setInputValue(option);
        setShowDropdown(false); // Скрыть выпадающий список после выбора
    };

    return (
        <div>
            <Form.Group>
                <InputGroup>
                    <InputGroup.Text id="basic-addon1">@</InputGroup.Text>
                    <Form.Control
                        value={inputValue}
                        onChange={handleInputChange}
                        placeholder="Username"
                        aria-label="Username"
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
};

export default DropdownInput;