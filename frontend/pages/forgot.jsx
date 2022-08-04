import Main from "layouts/main";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as Yup from "yup";
import { userService } from "service/user.service";
import { toast } from "react-toastify";

const forgot = () => {
  const validationSchema = Yup.object().shape({
    emailID: Yup.string().email().required("Enter your Email ID"),
  });
  const formOptions = { resolver: yupResolver(validationSchema) };
  const { register, handleSubmit, formState } = useForm(formOptions);
  const { errors } = formState;

  function onSubmit(user) {
    const toSubmit = {
      emailID: user.emailID,
    };
    return userService
      .forgot(toSubmit)
      .then((data) => {
        if (data["statusCode"] !== 200) {
          toast.error(`Error: ${JSON.stringify(data["message"])}`);
        } else {
          toast.success("Password reset link sent to your registered email id");
        }
      })
      .catch((e) => {
        toast.error("There was an error");
      });
  }

  return (
    <Main title="Forgot Password">
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="grid grid-cols-1 gap-2 w-full lg:w-96">
          <label htmlFor="emailID">Enter registered email:</label>
          <input
            className="bg-slate-50 outline-none p-2 rounded-lg border-2 border-black"
            type="email"
            name="emailID"
            id="emailID"
            placeholder="Email"
            {...register("emailID")}
          />
          <p className="text-red-600">{errors.emailID?.message}</p>
          <button
            className="border-black border-2 rounded-md font-semibold hover:bg-black hover:text-white p-2 mt-4"
            type="submit"
          >
            Submit
          </button>
        </div>
      </form>
    </Main>
  );
};

export default forgot;
