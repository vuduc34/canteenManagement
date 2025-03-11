import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ActiveAccountComponent } from './active-account/active-account.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { CartComponent } from './cart/cart.component';
import { OrderHistoryComponent } from './order-history/order-history.component';
// import { IntroduceComponent } from '../introduce/introduce.component';

@NgModule({
    imports: [RouterModule.forChild([
        // { path: 'error', loadChildren: () => import('./error/error.module').then(m => m.ErrorModule) },
        // { path: 'access', loadChildren: () => import('./access/access.module').then(m => m.AccessModule) },
        // { path: 'login', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },
        // { path: 'forgot-password', component:ForgotPasswordComponent},
        // { path: 'reset-password',  component: ResetPasswordComponent},
        // { path: 'signup', component: SignUpComponent},
        // { path: 'active', component: ActiveAccountComponent},
        // { path: 'cart', component: CartComponent},
        // { path: 'order-history', component: OrderHistoryComponent},


        { path: 'error', loadChildren: () => import('./error/error.module').then(m => m.ErrorModule) },
        { path: 'access', loadChildren: () => import('./access/access.module').then(m => m.AccessModule) },
        { path: 'dang-nhap', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },
        { path: 'quen-mat-khau', component:ForgotPasswordComponent},
        { path: 'khoi-phuc-mat-khau',  component: ResetPasswordComponent},
        { path: 'dang-ky', component: SignUpComponent},
        { path: 'kich-hoat-tai-khoan', component: ActiveAccountComponent},
        { path: 'gio-hang', component: CartComponent},
        { path: 'lich-su', component: OrderHistoryComponent},
        // { path: 'gioi-thieu', component: IntroduceComponent},

    ])],
    exports: [RouterModule]
})
export class AuthRoutingModule { }
