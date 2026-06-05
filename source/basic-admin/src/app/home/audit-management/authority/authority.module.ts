import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {NzButtonModule, NzModalModule, NzTabsModule} from 'ng-zorro-antd';
import {AuthorityDetailsComponent} from './authority-details/authority-details.component';
import {AuthorityAuditComponent} from './authority-audit/authority-audit.component';
import {AuthorityRoutingModule} from './authority-routing.module';
import {TableFindModule} from '../../../../../projects/xc-common/src/common/table-find/table-find.module';
import {FormInfoModule} from '../../../../../projects/xc-common/src/common/form-info/form-info.module';
import {TableDetailModule} from '../../../../../projects/xc-common/src/common/table-detail/table-detail.module';


@NgModule({
  declarations: [AuthorityAuditComponent, AuthorityDetailsComponent],
  imports: [
    CommonModule,
    AuthorityRoutingModule,
    TableFindModule,
    FormInfoModule,
    NzModalModule,
    NzButtonModule,
    TableDetailModule,
    NzTabsModule
  ]
})
export class AuthorityModule {
}
