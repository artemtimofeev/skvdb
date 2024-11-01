import './App.css';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import {Home} from "./pages/Home";
import {SignUp} from "./pages/SignUp";
import {Login} from "./pages/Login";
import {Instances} from "./pages/Instances";
import {Instance} from "./pages/Instance";
import {NotFound} from "./pages/NotFound";
import {Billing} from "./pages/Billing";

function App() {
  return (
      <BrowserRouter>
          <Routes>
              <Route path="/" Component={Home}/>
              <Route path="/signup" Component={SignUp}/>
              <Route path="/login" Component={Login}/>
              <Route path="/instances" Component={Instances}/>
              <Route path="/instance/:instanceId" Component={Instance}/>
              <Route path="/billing" Component={Billing}/>
              <Route path="*" Component={NotFound}/>
          </Routes>
      </BrowserRouter>
  );
}

export default App;
