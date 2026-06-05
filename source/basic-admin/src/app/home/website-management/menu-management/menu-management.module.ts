import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MenuManagementRoutingModule } from './menu-management-routing.module';
import {TableFindModule} from '../../../../../projects/xc-common/src/public-api';
import {FormInfoModule} from '../../../../../projects/xc-common/src/public-api';
import {MenuListComponent} from './menu-list/menu-list.component';
import {NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {FormsModule} from '@angular/forms';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/public-api';

@NgModule({
  declarations: [MenuListComponent],
  imports: [
    CommonModule,
    FormsModule,
    NzFormModule,
    TableFindModule,
    FormInfoModule,
    NzSelectModule,
    NzModalModule,
    NzInputModule,
    OverallSelectModule,
    MenuManagementRoutingModule
  ]
})
export class MenuManagementModule { }
