import { Component } from '@angular/core';
import { Signin } from './signin/signin';
import { Signup } from './signup/signup';
type Tabs = 'signin' | 'signup';

@Component({
  selector: 'register',
  imports: [Signin, Signup],
  templateUrl: './register.html',
  styleUrl: './register.css'
})

export class Register {
  activeTab: Tabs = 'signin';

  setActiveTab(activeTab: Tabs) {
    this.activeTab = activeTab;
    
  }
}