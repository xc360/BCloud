import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthorizeManagementRoutingModule } from './authorize-management-routing.module';
import { AuthorizeListComponent } from './authorize-list/authorize-list.component';
import {TableFindModule} from '../../../../projects/xc-common/src/public-api';
import { AuthorizeAuthorityListComponent } from './authorize-authority-list/authorize-authority-list.component';

@NgModule({
  declarations: [AuthorizeListComponent, AuthorizeAuthorityListComponent],
  imports: [
    CommonModule,
    TableFindModule,
    AuthorizeManagementRoutingModule
  ]
})
export class AuthorizeManagementModule { }
