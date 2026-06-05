import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserManagementRoutingModule } from './user-management-routing.module';
import {TableFindModule} from '../../../../../projects/xc-common/src/public-api';
import {FormInfoModule} from '../../../../../projects/xc-common/src/public-api';
import {NzFormModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {UserListComponent} from './user-list/user-list.component';
import {FormsModule} from '@angular/forms';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/public-api';
import { UserDetailsComponent } from './user-details/user-details.component';
import {TableDetailModule} from '../../../../../projects/xc-common/src/public-api';

@NgModule({
  declarations: [UserListComponent, UserDetailsComponent],
  imports: [
    CommonModule,
    FormsModule,
    NzFormModule,
    TableFindModule,
    FormInfoModule,
    NzSelectModule,
    NzModalModule,
    TableDetailModule,
    OverallSelectModule,
    UserManagementRoutingModule
  ]
})
export class UserManagementModule { }
