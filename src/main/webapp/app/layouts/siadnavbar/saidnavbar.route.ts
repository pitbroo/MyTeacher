import { Route } from '@angular/router';


import {SiadnavbarComponent} from "./siadnavbar.component";

export const navbarRoute: Route = {
  path: '',
  component: SiadnavbarComponent,
  outlet: 'siadnavbar',
};
