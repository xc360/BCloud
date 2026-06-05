import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SupplierManagementRoutingModule } from './supplier-management-routing.module';
import { SupplierListComponent } from './supplier-list/supplier-list.component';
import {FormsModule} from '@angular/forms';
import {NzButtonModule, NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {TableFindModule} from '../../../../../projects/xc-common/src/common/table-find/table-find.module';
import {FormInfoModule} from '../../../../../projects/xc-common/src/common/form-info/form-info.module';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/common/overall-select/overall-select.module';


@NgModule({
  declarations: [SupplierListComponent],
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
    SupplierManagementRoutingModule
  ]
})
export class SupplierManagementModule { }
