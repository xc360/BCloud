import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InfoManagementRoutingModule } from './info-management-routing.module';
import {TableFindModule} from '../../../../../projects/xc-common/src/public-api';
import {FormInfoModule} from '../../../../../projects/xc-common/src/public-api';
import {FormsModule} from '@angular/forms';
import {NzButtonModule, NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {InfoListComponent} from './info-list/info-list.component';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/public-api';
import { InfoEditComponent } from './info-edit/info-edit.component';

@NgModule({
  declarations: [InfoListComponent, InfoEditComponent],
  imports: [
    CommonModule,
    FormsModule,
    NzFormModule,
    TableFindModule,
    FormInfoModule,
    NzSelectModule,
    NzModalModule,
    NzInputModule,
    NzButtonModule,
    OverallSelectModule,
    InfoManagementRoutingModule
  ]
})
export class InfoManagementModule { }
