import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {NzButtonModule, NzModalModule, NzTabsModule} from 'ng-zorro-antd';
import {AppDetailsComponent} from './app-details/app-details.component';
import {TableFindModule} from '../../../../../projects/xc-common/src/common/table-find/table-find.module';
import {FormInfoModule} from '../../../../../projects/xc-common/src/common/form-info/form-info.module';
import {TableDetailModule} from '../../../../../projects/xc-common/src/common/table-detail/table-detail.module';
import {AppRoutingModule} from './app-routing.module';
import {AppAuditComponent} from './app-audit/app-audit.component';


@NgModule({
  declarations: [AppAuditComponent, AppDetailsComponent],
  imports: [
    CommonModule,
    AppRoutingModule,
    TableFindModule,
    FormInfoModule,
    NzModalModule,
    NzButtonModule,
    TableDetailModule,
    NzTabsModule
  ]
})
export class AppModule {
}
