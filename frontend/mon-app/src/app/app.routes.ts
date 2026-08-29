import { Routes } from '@angular/router';
import { Register } from './register/register';
export const routes: Routes = [
  {
    path: '',
    redirectTo : 'register',
    pathMatch : 'full'
  },
  {
    path: 'register',
    component: Register
  }
    
];
