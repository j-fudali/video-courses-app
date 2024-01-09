export interface JwtToken {
  exp: number;
  iat: number;
  role: string;
  userId: number;
  sub: string;
}
