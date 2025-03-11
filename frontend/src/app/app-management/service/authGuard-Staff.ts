import { Injectable } from "@angular/core";
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from "@angular/router";
import { AppRoutingUrl } from "../app-routing.url";
import { AuthService } from "./auth.service";

@Injectable()
export class AuthGuardStaff implements CanActivate {
    constructor(private router: Router,private authService:AuthService) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (this.authService.getRole() == "ROLE_STAFF"||this.authService.getRole()== "ROLE_ADMIN") {
            return true;
        }
        else {
            this.router.navigate([AppRoutingUrl.common.access_denied]);
            return false;

        }

    }
}