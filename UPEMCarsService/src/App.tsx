import { AppBarCustomized } from './components/app-bar';

import * as React from 'react';
import './App.css';

class App extends React.Component {
  public render() {
    return (
      <div className="App">
        <header className="App-header">
          <AppBarCustomized/>
        </header>

      </div>
    );
  }
}

export default App;
