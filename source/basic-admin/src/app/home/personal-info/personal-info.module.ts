import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PersonalInfoRoutingModule} from './personal-info-routing.module';
import {PersonalInfoDetailsComponent} from './personal-info-details/personal-info-details.component';
import {TableDetailModule} from '../../../../projects/xc-common/src/public-api';
import {FormInfoModule} from '../../../../projects/xc-common/src/public-api';
import {NzButtonModule, NzModalModule} from 'ng-zorro-antd';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzGridModule} from 'ng-zorro-antd/grid';
import {FormsModule} from '@angular/forms';
import {NzInputModule} from 'ng-zorro-antd/input';

@NgModule({
  declarations: [PersonalInfoDetailsComponent],
  imports: [
    CommonModule,
    TableDetailModule,
    PersonalInfoRoutingModule,
    NzButtonModule,
    FormInfoModule,
    NzModalModule,
    NzFormModule,
    NzGridModule,
    FormsModule,
    NzInputModule
  ]
})
export class PersonalInfoModule {
}
