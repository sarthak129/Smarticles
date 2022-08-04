import Main from "layouts/main";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import router from "next/router";
import * as Yup from "yup";
import { userService } from "service/user.service";
import { toast } from "react-toastify";

const Reset = () => {
  const { token } = router.query;
  const validationSchema = Yup.object().shape({
    pswd: Yup.string()
      .required("Create new password for your account")
      .min(8, "Use 8 characters or more for your password"),
    confirmpassword: Yup.string().oneOf([Yup.ref("pswd"), null]),
  });

  const formOptions = { resolver: yupResolver(validationSchema) };
  const { register, handleSubmit, formState } = useForm(formOptions);
  const { errors } = formState;

  function onSubmit(user) {
    const toSubmit = {
      pswd: user.pswd,
      token,
    };
    return userService
      .reset(toSubmit)
      .then((data) => {
        if (data["statusCode"] !== 200) {
          toast.error(`Error: ${JSON.stringify(data["message"])}`);
        } else {
          toast.success("Password changed Successfully");
          window.setTimeout(function () {
            router.push("/login");
          }, 3000);
        }
      })
      .catch((e) => {
        toast.error("There was an error");
      });
  }

  return (
    <Main title="Reset">
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="grid grid-cols-1 gap-3 w-full lg:w-96">
          <div className="flex flex-col">
            <label htmlFor="pswd">Password</label>
            <input
              className="bg-slate-50 outline-none p-2 rounded-lg border-2 border-black"
              type="password"
              name="pswd"
              id="pswd"
              placeholder="Password"
              {...register("pswd")}
            />
            <p className="text-red-600">{errors.pswd?.message}</p>
          </div>
          <div className="flex flex-col">
            <label htmlFor="confirmpassword">Confirm Password</label>
            <input
              className="bg-slate-50 outline-none p-2 rounded-lg border-2 border-black"
              type="password"
              name="confirmpassword"
              id="confirmpassword"
              placeholder="Confirm password"
              {...register("confirmpassword")}
            />
            <p className="text-red-600">
              {errors.confirmpassword &&
                "Those passwords didn’t match. Please try again."}
            </p>
          </div>
          <button
            className="border-black border-2 rounded-md font-semibold hover:bg-black hover:text-white mt-4 p-2"
            type="submit"
          >
            Reset Password
          </button>
        </div>
      </form>
    </Main>
  );
};

export default Reset;
